(ns csk.core
    (:require [csk.serializer :as csr])
    (:import
      (com.esotericsoftware.kryo Kryo Serializer)
      ))

(defn register-serializers
  "Register a map of Class to Kryo Serializer with a Kryo registry."
  [^Kryo kryo serializers]
  (doseq [[^Class klass ^Serializer serializer] serializers]
         (.register kryo klass serializer))
  kryo)

(defn kryo
  ([]
   (kryo (Kryo.)))
  ([^Kryo k]
   (register-serializers k csr/csk-default-serializers)))
