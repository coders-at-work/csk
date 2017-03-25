(ns csk.t-serialize
    (:require [midje.sweet :refer :all]
              [csk.serialize :refer :all]
              )
    (:import (com.esotericsoftware.kryo.io Input Output)
             (com.esotericsoftware.kryo Kryo)
             )
    )

(facts "Can serialize and deserialize primitive objects with Output and Input"
       (let [o (Output. 1000)
             b (.getBuffer o)
             i (Input. b)
             ]
         (fact "Can serialize and deserialize nil"
               (write-obj-str o nil)
               (read-obj-str i) => nil
               )
         (fact "Can serialize and deserialize boolean"
               (write-obj-str o true)
               (read-obj-str i) => true
               (write-obj-str o false)
               (read-obj-str i) => false
               )
         (fact "Can serialize and deserialize byte value, but not keeping its type"
               (write-obj-str o (byte 1))
               (let [v (read-obj-str i)]
                 v => 1
                 (instance? Byte v) =not=> true
                 ))
         (fact "Can serialize and deserialize short value, but not keeping its type"
               (write-obj-str o (short 1))
               (let [v (read-obj-str i)]
                 v => 1
                 (instance? Short v) =not=> true
                 ))
         (fact "Can serialize and deserialize integer value, but not keeping its type"
               (write-obj-str o (int 1))
               (let [v (read-obj-str i)]
                 v => 1
                 (instance? Integer v) =not=> true
                 ))
         (fact "Can serialize and deserialize long"
               (write-obj-str o 1)
               (let [v (read-obj-str i)]
                 v => 1
                 (instance? Long v) => true
                 ))
         (fact "Can serialize and deserialize char"
               (write-obj-str o \a)
               (let [v (read-obj-str i)]
                 v => \a 
                 (instance? Character v) => true
                 ))
         (fact "Can serialize and deserialize string"
               (write-obj-str o "abc")
               (read-obj-str i) => "abc"
               )
         (fact "Can serialize and deserialize BigInt"
               (write-obj-str o 1N)
               (let [v (read-obj-str i)]
                 v => 1N
                 (instance? clojure.lang.BigInt v) => true
                 ))
         (fact "Can serialize and deserialize java.math.BigDecimal"
               (write-obj-str o 1M)
               (let [v (read-obj-str i)]
                 v => 1M
                 (instance? java.math.BigDecimal v) => true
                 ))
         (fact "Can serialize and deserialize keyword"
               (write-obj-str o :a)
               (read-obj-str i) => :a
               )
         (fact "Can serialize and deserialize symbol"
               (write-obj-str o 'a)
               (read-obj-str i) => 'a
               )
       ))

(facts "Can serialize clojure collection and deserialize it as a clojure seq by default"
       (let [o (Output. 1000)
             b (.getBuffer o)
             i (Input. b)
             k (Kryo.)
             ]
         (fact "can serialize and deserialize ISeq"
               (write-collection k o '(1 2 3))
               (read-collection k i identity) => '(1 2 3)
               )
        ))
