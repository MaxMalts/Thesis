import cv2
import math
import numpy as np
import tensorflow as tf
import tensorflow_addons as tfa
from deskew import determine_skew


def extract_lines(filePath):
    image_width = 2048
    
    img = cv2.imread(filePath, cv2.IMREAD_GRAYSCALE)
    img = _preprocess_image(img, image_width)
    
    char_contours = _find_contours(img)
    char_contours = _filter_contours(char_contours)
    
    img_char_lines = _add_lines(img, char_contours, 10)
    word_contours = _find_contours(img_char_lines)
    word_contours = _filter_contours(word_contours)
    
    img_word_lines = _add_lines(img, word_contours, 100)
    line_contours = _find_contours(img_word_lines)
    line_contours = _filter_contours(line_contours)
    
    line_contours = sorted(line_contours, key = lambda ctr : cv2.boundingRect(ctr)[1]) # (x, y, w, h)
    
    lines = []
    for ctr in line_contours:
        x, y, w, h = cv2.boundingRect(ctr)
        lines.append(img[y : y + h, x : x + w])
    
    return lines


def _rotate(image, angle, padding_color):
    orig_h, orig_w = image.shape[0], image.shape[1]
    angle_radian = math.radians(angle)
    new_height = abs(np.sin(angle_radian) * orig_w) + abs(np.cos(angle_radian) * orig_h)
    new_width = abs(np.sin(angle_radian) * orig_h) + abs(np.cos(angle_radian) * orig_w)

    rot_mat = cv2.getRotationMatrix2D((orig_w // 2, orig_h // 2), angle, 1.0)
    rot_mat[1, 2] += (new_height - orig_h) / 2
    rot_mat[0, 2] += (new_width - orig_w) / 2
    return cv2.warpAffine(image, rot_mat, (int(new_width), int(new_height)), borderValue=padding_color)


def _bradley_thresholding(image):
    kernel_size = 31
    percent_lower = 6
    sigma = 7 # or ((kernel_size ** 2 - 1) / 12) ** 0.5 https://stackoverflow.com/questions/35340197/box-filter-size-in-relation-to-gaussian-filter-sigma
    
    average_around_pixel = cv2.GaussianBlur(image, (kernel_size, kernel_size), sigma)
    
    res = np.where(image < average_around_pixel * (100.0 - percent_lower) / 100.0, 0.0, 255.0)
    return res


def _preprocess_image(image, image_width):
    image = image.astype(np.float32)
    image = cv2.resize(image, (image_width, int(image.shape[0] * image_width / image.shape[1])))
    image = cv2.GaussianBlur(image, (5, 5), 1.5)
    
    image = _bradley_thresholding(image).astype(np.uint8)
    
    #dilation
    # kernel = np.ones((3, 3), np.uint8)
    # dilated = 255 - cv2.dilate(255 - preprocessed_img, kernel, iterations = 1)
    
    angle = determine_skew(image)
    if not (angle is None):
        image = _rotate(image, angle, 255)  # size changed
        image = cv2.resize(image, (image_width, int(image.shape[0] * image_width / image.shape[1])))
    
    return image


def _find_contours(image):
    contours, _ = cv2.findContours(255 - image, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_TC89_L1)
    return contours


def _filter_contours(contours):
    area_threshold_percent = 30
    overlapping_threshold_percent = 50
    
    # area thresholding:
    mean_area = np.mean([cv2.contourArea(contour) for contour in contours])
    area_filtered = []
    for contour in contours:
        if cv2.contourArea(contour) > mean_area * area_threshold_percent / 100:
            area_filtered.append(contour)
    
    # overlap thresholding:
    area_filtered = sorted(area_filtered, key = lambda ctr: -(cv2.boundingRect(ctr)[2] * cv2.boundingRect(ctr)[3]))
    overlap_filtered = [area_filtered[0]]
    for i in range(1, len(area_filtered)):
        inner_contour = area_filtered[i]
        overlaped = False
        for j in range(i):
            outer_contour = area_filtered[j]
            x_inner, y_inner, w_inner, h_inner = cv2.boundingRect(inner_contour)
            x_outer, y_outer, w_outer, h_outer = cv2.boundingRect(outer_contour)
            
            x_inter = max(x_inner, x_outer)
            w_inter = min(x_inner + w_inner, x_outer + w_outer) - x_inter
            y_inter = max(y_inner, y_outer)
            h_inter = min(y_inner + h_inner, y_outer + h_outer) - y_inter
            
            if w_inter > 0 and h_inter > 0 and w_inter * h_inter / (w_inner * h_inner) > overlapping_threshold_percent / 100:
                overlaped = True
                break
        
        if not overlaped:
            overlap_filtered.append(inner_contour)
            
    return tuple(overlap_filtered)


def _add_lines(image, contours, line_len):
    width = 5
    
    res_img = image.copy()
    for ctr in contours:
        x, y, w, h = cv2.boundingRect(ctr)
        y_mid = y + h // 2
        cv2.line(res_img, (x - line_len, y_mid), (x + w + line_len, y_mid), 0, width)
    
    return res_img