import re
import numpy as np
import tensorflow as tf
import tensorflow_addons as tfa
from word_beam_search import WordBeamSearch


class handwriting_recognizer:
    
    model_file = '/home/max/Documents/Thesis/text-recognizer/model/model (also good).h5'
    corpus_file = '/home/max/Documents/Thesis/text-recognizer/words_database/american-english.txt'
    recognized_chars = ' !"#&\'()*+,-./0123456789:;?ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'
    word_chars = 'QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm'
    padding_token = len(recognized_chars) + 10
    empty_token = -1
    
    
    def __init__(self):        
        self.char_num_map = self.CharNumMap(list(self.recognized_chars))
        
        f = open(self.corpus_file, 'r', encoding='utf-8')
        corpus = f.read()
        f.close()
        self.beam_search = WordBeamSearch(25, 'Words', 0.0, corpus.encode('utf8'),
                                          ''.join(self.recognized_chars).encode('utf8'), self.word_chars.encode('utf8'))
        
        self.model = tf.keras.models.load_model(self.model_file, compile=False)
        self.image_height = self.model.layers[0].input_shape[0][1]
        self.image_width = self.model.layers[0].input_shape[0][2]
        
        if (self.model.layers[0].input_shape[0][3] != 1):
            raise Exception("Model does not expect a grayscale image")


    def recognize_lines(self, grayscaleImages, are_binarized=False):
        preprocessed = [self.__preprocess_image(image[:, :, np.newaxis], not are_binarized) for image in grayscaleImages]
        return self.__predict_lines(tf.convert_to_tensor(preprocessed))
    
    
    def __bradley_thresholding(self, image):
        kernel_size = 32
        percent_lower = 6
        sigma = 7 # or ((kernel_size ** 2 - 1) / 12) ** 0.5 https://stackoverflow.com/questions/35340197/box-filter-size-in-relation-to-gaussian-filter-sigma
        
        average_around_pixel = tfa.image.gaussian_filter2d(image, filter_shape=kernel_size, sigma=sigma)
        
        res = tf.where(tf.cast(image, tf.float32) < average_around_pixel * (100 - percent_lower) / 100, 0, 255)
        return res
    
    
    def __preprocess_image(self, image, do_threshold=True):    
        if do_threshold:
            image = tf.image.resize(image, size=(self.image_height, self.image_width), preserve_aspect_ratio=True)  # to make bluring consistent
            image = tfa.image.gaussian_filter2d(image, filter_shape=6, sigma=1.5)
            image = self.__bradley_thresholding(image)
        
        image = tf.image.resize_with_pad(image, self.image_height, self.image_width)
        image = tf.cast(image, tf.float32) / 255.0
        return image
    
    
    def __predict_lines(self, images, do_threshold=True):
        chars_no_space_before = '!),.:;?'
        chars_no_space_after = '('
            
        pred = self.model.predict(images)
        pred_sequences = self.__pred_batch_to_sequences(pred)
        
        pred_lines = self.__sequences_batch_to_words(pred_sequences)
        pred_lines = [re.sub(f' ([{chars_no_space_before}])', '\g<1>', line) for line in pred_lines]
        pred_lines = [re.sub(f'([{chars_no_space_after}]) ', '\g<1>', line) for line in pred_lines]
        
        return pred_lines
    

    def __pred_batch_to_sequences(self, pred):
        # removing the penultimate element from every sequence:
        pred_fixed = tf.concat([pred[:, :, :-2], pred[:, :, -1:]], axis=-1)
        
        res = self.beam_search.compute(tf.transpose(pred_fixed, (1, 0, 2)).numpy())
        res = tf.ragged.constant(res, dtype=tf.int64).to_tensor(self.empty_token, shape=[tf.shape(pred)[0], tf.shape(pred)[1]])
        return res


    def __sequences_batch_to_words(self, sequences):        
        lines = []
        for sequence in sequences:
            sequence = tf.gather(sequence, tf.where(tf.math.not_equal(sequence, self.empty_token)))
            sequence = tf.reshape(sequence, [-1])
            sequence = tf.strings.reduce_join(self.char_num_map.num_to_char(sequence)).numpy().decode("utf-8")
            lines.append(sequence)
        
        return lines
    
    
    class CharNumMap:
        def __init__(self, characters):
            self.num_to_char_map = tf.constant(characters)
        
        def num_to_char(self, num):
            return tf.map_fn(lambda cur_num: self.num_to_char_map[cur_num], num, fn_output_signature=tf.string)
        
        def char_to_num(self, char):
            return tf.map_fn(lambda cur_char: tf.argmax(tf.where(self.num_to_char_map == cur_char, 1, 0)), char, fn_output_signature=tf.int64)
        
        def get_characters(self):
            return self.num_to_char_map.numpy().tolist()