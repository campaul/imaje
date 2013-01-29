(ns imaje.processing)

(import java.awt.image.BufferedImage
        java.io.ByteArrayOutputStream
        java.io.ByteArrayInputStream
        javax.imageio.ImageIO
        java.awt.Color)

(defn load-image [file]
  (ImageIO/read (file :tempfile)))

(defn save-image [image]
  (let [out (ByteArrayOutputStream.)]
    (do
      (ImageIO/write image "png" out)
      (ByteArrayInputStream. (.toByteArray out)))))

(defn get-bytes [image]
  (.getData
    (.getDataBuffer
      (.getRaster image))))

(defn getPixel [bytes index]
  [(nth bytes index)
   (nth bytes (+ 1 index))
   (nth bytes (+ 2 index))])

(defn setPixel [bytes index [r g b]]
  (do
    (aset-byte bytes index r)
    (aset-byte bytes (+ index 1) g)
    (aset-byte bytes (+ index 2) b)))

(defn filter-bytes [index bytes fltr]
  (if
    (< index (count bytes))
    (do
      (setPixel bytes index (fltr (getPixel bytes index)))
      (recur (+ index 3) bytes fltr))))

(defn filter-image [image fltr] 
  (do
    (filter-bytes 0 (get-bytes image) fltr) image))
