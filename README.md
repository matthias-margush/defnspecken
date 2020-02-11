Tasty `defn`s with `s/fdef`s baked in.

``` clojure
(ns my-namespace
  (:require [specken :refer [defnspeck]]
            [clojure.spec.alpha :as s]))
```

## Usage
This:
``` clojure
(defnspeck ranged-rand
  "Returns random int in range start <= rand < end"
  {::s/args (s/and (s/cat :start int? :end int?)
                   #(< (:start %) (:end %)))
   ::s/ret int?
   ::s/fn (s/and #(>= (:ret %) (-> % :args :start))
                 #(< (:ret %) (-> % :args :end)))}
  [start end]
  (+ start (long (rand (- end start)))))
```

Results in this:
``` clojure
(defn ranged-rand
  "Returns random int in range start <= rand < end"
  [start end]
  (+ start (long (rand (- end start)))))

(s/fdef ranged-rand
  :args (s/and (s/cat :start int? :end int?)
               #(< (:start %) (:end %)))
  :ret int?
  :fn (s/and #(>= (:ret %) (-> % :args :start))
             #(< (:ret %) (-> % :args :end))))
```

## License

Copyright © 2018 Matthias Margush

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
