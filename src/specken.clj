(ns specken
  "Defn with spec baked in."
  (:refer-clojure :exclude [defn])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as st]))

(defn- attr-map?
  "If a clojure form is an attribute map, returns it."
  [s]
  (when (map? s) s))

(defn- specken-defn*
  "Creates an s/fdef form from the meta on a function definition."
  [f]
  (let [attr-map (or (attr-map? (nth f 1))
                     (attr-map? (nth f 2))
                     (attr-map? (last f)))
        {::s/keys [args ret fn]} attr-map]
    (when (or args ret fn)
      `(s/fdef ~(first f)
         ~@(when args `(:args ~args))
         ~@(when ret `(:ret ~ret))
         ~@(when fn `(:fn ~fn))))))

(defmacro defn
  "Given a function definition, emits the original defn, along with an
  s/fdef form using the ::s/args, ::s/ret, and ::s/fn meta
  attributes."
  [& f]
  `(do (clojure.core/defn ~@f) ~(specken-defn* f)))
