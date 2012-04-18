(ns grass-reader.test.core
  (:use [grass-reader.core])
  (:use [clojure.test]))

(deftest parsing
  (is (= [[:abs 1 []]
          [:abs 2 [[:app 2 1]]]
          [:abs 1 [[:app 1 2] [:app 3 1]]]]
         (parse-grass "wvwwWWwvwWwwWWWw"))))

(defmacro mkfn [code]
  (make-form code []))

(deftest ski
  (let [S (mkfn [:abs 3 [[:app 3 1] [:app 3 2] [:app 2 1]]])
        K (mkfn [:abs 0 [[:abs 1 []] [:abs 2 [[:app 3 2]]]]])
        I (mkfn [:abs 1 []])]
    (is (= :arg
           (((S K) K) :arg)
           (I :arg)))))

(deftest church-num
  (is (= 256 ((#grass wwWWwWWWwvWwWw inc) 0))))

(deftest read-skk
  (let [S #grass wvwwwWWWwWWWwwWWwvWWw
        K #grass wvwwWWWwwvWWw]
    (is (= :arg (((S K) K) :arg)))))

(deftest y-combinator
  (is (= 3628800
         ((#grass wwWWwwWwwvwwWWWwWWWwvwWWwWwvwvWww
                  (fn [f] (fn [x] (if (= x 1) 1 (* x (f (dec x)))))))
          10))))
