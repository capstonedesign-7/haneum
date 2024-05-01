import cv2
import easyocr

parsedsc = open("parsedSC.txt",'a',encoding='utf-8')
reader = easyocr.Reader(['ko'], gpu = True)

for i in range(-):
    imgpath = "scr ("+str(i+1)+").jpg"
    img = cv2.imread(imgpath)
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    result = reader.readtext(imgpath)
    parsed_text = reault.split('\n')
    print(parsed_text)

    for txt in parsed_text:
        parsedsc.write('\n')
        parsedsc.write(txt)
    print(imgpath+" done")
    

parsedsc.close()
