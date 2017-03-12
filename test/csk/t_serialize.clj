(ns csk.t-serialize
    (:require [midje.sweet :refer :all]
              [csk.serialize :refer :all]
              )
    (:import (com.esotericsoftware.kryo.io Input Output))
    )

(facts "Can serialize and deserialize primitive objects with Output and Input"
       (fact "Can serialize and deserialize Boolean"
             (let [o (Output. 1000)
                   b (.getBuffer o)
                   i (Input. b)
                   ]
               (write-obj-str o true)
               (read-obj-str i) => true
               (write-obj-str o false)
               (read-obj-str i) => false
               ))
       )
