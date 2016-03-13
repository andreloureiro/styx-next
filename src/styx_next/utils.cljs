(ns styx-next.utils
  (:require [replumb.core :as replumb])
  (:import [goog.ui IdGenerator]))

(defn guid []
  (.getNextUniqueId (.getInstance IdGenerator)))

(defn handle-result!
  [result]
  result)

(defn cljs-read-eval-print!
  [user-input]
  (replumb/read-eval-call (partial handle-result!) user-input))

(def ENTER_KEY 13)

(def ARROW_UP_KEY 38)

(def ARROW_DOWN_KEY 40)
