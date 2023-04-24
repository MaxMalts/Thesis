fin = open("american-english-orig.txt", "r")
words = fin.read()
fin.close()

words = words.splitlines()

words = [word for word in words if (len(word) < 2 or word[-2:] != "'s")]

words_upper = [word[0].upper() + (word[1:] if len(word) > 1 else '') for word in words]
words_all_upper = [word.upper() for word in words]
words = set(words)
words.update(words_upper)
words.update(words_all_upper)
words = '\n'.join(list(words))

fout = open("american-english.txt", "w")
fout.writelines(words)
fout.close()
