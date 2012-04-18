# grass-reader

[Grass](http://www.blue.sky.or.jp/grass/)のコードを関数として読み込みます。
プリミティブの実装を省略して初期environmentが空になっているので
`wWWwwww`といったコードはエラーになります。コンビネータの記述等にお使い下さい。


## Usage

    ;;; Y combinator
    ((#grass wwWWwwWwwvwwWWWwWWWwvwWWwWwvwvWww
      (fn [f] (fn [x] (if (= x 1) 1 (* x (f (dec x)))))))
     10)
    ; => 3628800


## License

Copyright (C) 2012 @Yubeshi

Distributed under the Eclipse Public License, the same as Clojure.
