(ns styx-next.core
  (:require [goog.dom :as gdom]
            [om.dom :refer [div h3]]
            [om.next :as om :refer-macros [defui]]
            [replumb.core :as replumb]
            [styx-next.components.cmd-history :refer [cmd-history]]
            [styx-next.components.cmd-input :refer [cmd-input]]
            [styx-next.components.cmd-item :refer [CmdItem]]
            [styx-next.reconciler :refer [reconciler]]
            [styx-next.utils :refer [cljs-read-eval-print! guid ENTER_KEY ARROW_UP_KEY ARROW_DOWN_KEY]]))

(enable-console-print!)

(defn ^:private styx-template
  [this list props]
  (div #js {:className "styx"}
       (cmd-history list)
       (cmd-input
        (om/computed props {:handle-input #(.handle-cmd-input this %)}))))

(defn handle-enter-key [this e enter-key text]
  (when (not (clojure.string/blank? text))
    (do
      (.preventDefault e)
      (om/transact! this `[(command/add {:cmd ~(cljs-read-eval-print! text)}) :command/list])
      (set! (.. e -target -value) ""))))

(defn handle-arrow-keys [key-pressed this e]
  (let [{:keys [command/list history/active]} (om/props this)
        {:keys [history/index]} active]
    (do
      (.preventDefault e)
      (println "index " index)
      (om/transact! this `[(history/navigate ~{:key-code key-pressed
                                               :input-component (.-target e)})
                           :history/active]))))

(def handle-arrow-up-key (partial handle-arrow-keys ARROW_UP_KEY))

(def handle-arrow-down-key (partial handle-arrow-keys ARROW_DOWN_KEY))

(defui ^:once Styx
  static om/IQuery
  (query [this]
         `[:app/title :history/active {:command/list ~(om/get-query CmdItem)}])

  Object
  (handle-cmd-input [this e]
                    (let [text (.. e -target -value)
                          key-code (.. e -keyCode)
                          {:keys [command/list history/active]} (om/props this)
                          {:keys [history/index]} active]
                      (condp = key-code
                        ENTER_KEY (handle-enter-key this e ENTER_KEY text)
                        ARROW_UP_KEY (when (> index 0) (handle-arrow-up-key this e))
                        ARROW_DOWN_KEY (when (< index (count list)) (handle-arrow-down-key this e))
                        nil)))
  (render [this]
          (let [{:keys [app/title command/list history/active] :as props} (om/props this)]
            (styx-template this list props))))


(om/add-root!
 reconciler
 Styx
 (gdom/getElement "app"))
