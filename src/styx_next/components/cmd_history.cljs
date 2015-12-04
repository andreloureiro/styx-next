(ns styx-next.components.cmd-history
  (:require [om.dom :refer [ul div]]
            [om.next :as om :refer-macros [defui]]
            [replumb.core :as replumb]
            [styx-next.components.cmd-item :refer [cmd-item]]))

(defn- cmd-history-template
  [history]
  (div #js {:className "styx__history" :ref "historyContainer"}
       (apply ul #js {:className "history__box" :ref "historyList"} (map cmd-item history))))

(defui CmdHistory
  Object
  (componentDidUpdate [this _ _]
                      (let [history-container (.. this -refs -historyContainer)
                            history-list (.. this -refs -historyList)]
                        (set! (.-scrollTop history-container) (.-scrollHeight history-list))))
  (render [this]
          (let [history (om/props this)]
            (cmd-history-template history))))

(def cmd-history (om/factory CmdHistory))

