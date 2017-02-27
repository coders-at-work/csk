(ns csk.serializer
  (:require [csk.serialize :as cs])
  (:import
    (com.esotericsoftware.kryo Serializer)
    (clojure.lang Keyword Symbol BigInt
                  PersistentVector PersistentList PersistentList$EmptyList Cons LazySeq LongRange
                  IteratorSeq ArraySeq PersistentVector$ChunkedSeq PersistentArrayMap$Seq PersistentHashMap$NodeSeq
                  StringSeq APersistentMap$KeySeq MapEntry PersistentHashSet
                  PersistentArrayMap PersistentHashMap PersistentStructMap)
    (com.twitter.chill.java  RegexSerializer SqlDateSerializer SqlTimeSerializer
                            TimestampSerializer URISerializer UUIDSerializer)
    (java.util UUID)
    (java.util.regex Pattern)
    (java.net URI)
    (java.sql Date Time Timestamp)
    ))

;; simple primitive serializer
(def reader-serializer (proxy [Serializer] []
                         (write [k o v]
                           (cs/write-obj-str o v))
                         (read [k i c]
                           (cs/read-obj-str i))))


;; collection serializer
(defn create-coll-serializer
  [convert-fn]
  (proxy [Serializer] []
    (write [k o v]
      (cs/write-collection-str k o v))
    (read [k i c]
      (cs/read-collection-str k i convert-fn))))

(def list-serializer (create-coll-serializer #(apply list %)))
(def vector-serializer (create-coll-serializer #(vec %)))
(def cons-serializer (create-coll-serializer (fn [[e & rest]] (cons e rest))))
;should convert to a list first ;(def lazy-seq-serializer (create-coll-serializer #(lazy-seq %)))
(def string-seq-serializer (create-coll-serializer #(-> (apply str %) seq)))
(def vector-chunked-seq-serializer (create-coll-serializer #(-> % vec seq)))
(def map-entry-serializer (create-coll-serializer (fn [[k v]] (MapEntry/create k v))))
(def set-serializer (create-coll-serializer #(set %)))
(def array-map-serializer (create-coll-serializer #(->> (apply concat %) (apply array-map))))
(def hash-map-serializer (create-coll-serializer #(->> (apply concat %) (apply hash-map))))


;; serializers configuration
(def clojure-primitive-serializers [Keyword reader-serializer
                                    Symbol reader-serializer
                                    BigInt reader-serializer])

(def clojure-list-like-collection-serializers [PersistentList              list-serializer
                                               PersistentList$EmptyList    list-serializer
                                               Cons                        cons-serializer
                                               LazySeq                     list-serializer
                                               IteratorSeq                 list-serializer
                                               ArraySeq                    list-serializer
                                               PersistentVector$ChunkedSeq vector-chunked-seq-serializer
                                               StringSeq                   string-seq-serializer
                                               LongRange                   list-serializer
                                               PersistentArrayMap$Seq      list-serializer
                                               PersistentHashMap$NodeSeq   list-serializer
                                               APersistentMap$KeySeq       list-serializer])
(def clojure-vector-like-collection-serializers [PersistentVector vector-serializer
                                                 MapEntry         map-entry-serializer])
(def clojure-set-like-collection-serializers [PersistentHashSet set-serializer])
(def clojure-map-like-collection-serializers [PersistentArrayMap array-map-serializer
                                              PersistentHashMap hash-map-serializer
                                              PersistentStructMap hash-map-serializer])

(def clojure-collection-serializers (concat
                                      clojure-list-like-collection-serializers
                                      clojure-vector-like-collection-serializers
                                      clojure-set-like-collection-serializers
                                      clojure-map-like-collection-serializers
                                      ))

(def java-serializers [Timestamp     (TimestampSerializer.)
                       Date          (SqlDateSerializer.)
                       Time          (SqlTimeSerializer.)
                       URI           (URISerializer.)
                       Pattern       (RegexSerializer.)
                       UUID          (UUIDSerializer.)])

(def csk-default-serializers (concat java-serializers clojure-primitive-serializers clojure-collection-serializers))
