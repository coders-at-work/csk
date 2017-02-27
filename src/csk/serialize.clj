(ns csk.serialize
  (:require [clojure.string :as s])
  (:import
    (com.esotericsoftware.kryo Kryo)
    (com.esotericsoftware.kryo.io Input Output)
    ))

(defn write-obj-str
  "Converts an object to a string using clojure.core/pr-str. Then writes it into the Output."
  [^Output output obj]
  (.writeString output (pr-str obj)))

(defn read-obj-str
  "Reads a string from the Input. Then converts it to an object using clojure.core/read-string."
  [^Input input]
  (read-string (.readString input)))

(defn write-collection
  [^Kryo registry ^Output output coll]
  (.writeInt output (count coll) true)
  (doseq [x coll]
         (.writeClassAndObject registry output x)))

(defn read-collection
  [^Kryo registry ^Input input convert-fn]
  (let [len (.readInt input true)]
    (->> #(.readClassAndObject registry input)
         (repeatedly len)
         convert-fn)))
