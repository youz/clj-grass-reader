(ns grass-reader.core)

(defn parse-grass [src]
  (letfn [(parse-abs [code]
            (let [pos (.indexOf code (int \W))
                  arity (if (< pos 0) (count code) pos)]
              [:abs arity (parse-app (subs code arity))]))
          (parse-app [code]
            (map #(vector :app (count (% 1)) (count (% 2)))
                 (re-seq #"(W+)(w+)" code)))]
    (apply concat
           (for [code (.split (.replaceAll src "[^wWvV]" "") "[vV]")]
             (if (not (.matches code "^w*(W+w+)*$"))
               (throw (Exception. "grass syntax error"))
               (case (first code)
                 \w [(parse-abs code)]
                 \W (parse-app code)))))))

(defn make-form [[label arity code] dmp]
  (if (< 0 arity)
    (let [arg (gensym)]
      (list `fn [arg] (make-form [:abs (- arity 1) code] (cons arg dmp))))
    ((fn rec [[[label a b] & r] dmp]
       (if (nil? label)
         (first dmp)
         (let [p (gensym)]
           (list (list `fn [p] (rec r (cons p dmp)))
                 (case label
                   :app (list (nth dmp (dec a)) (nth dmp (dec b)))
                   :abs (make-form [label a b] dmp))))))
     code dmp)))

(defn grass-to-fn [s]
  (let [code (parse-grass (str s))]
    (if (= (first (last code)) :abs)
      (make-form [:abs 0 (concat code [[:app 1 1]])] ())
      (make-form [:abs 0 code] ()))))

(defmacro read-grass [s]
  (grass-to-fn s))

