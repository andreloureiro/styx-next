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
