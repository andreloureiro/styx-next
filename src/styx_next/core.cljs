(ns styx-next.core
  (:require [goog.dom :as gdom]
            [om.dom :refer [div h3]]
            [om.next :as om :refer-macros [defui]]
            [replumb.core :as replumb]
            [styx-next.components.cmd-history :refer [cmd-history]]
            [styx-next.components.cmd-input :refer [cmd-input]]
            [styx-next.components.cmd-item :refer [CmdItem]]
            [styx-next.reconciler :refer [reconciler]]
            [styx-next.utils :refer [cljs-read-eval-print! guid]]))

(enable-console-print!)

(defn- styx-template
  [this list props]
  (div #js {:className "styx"}
       (cmd-history list)
       (cmd-input
        (om/computed props {:handle-input #(.handle-cmd-input this %)}))))

(defui Styx
  static om/IQuery
  (query [this]
         `[:app/title {:command/list ~(om/get-query CmdItem)}])

  Object
  (handle-cmd-input [this e]
                    (let [text (.. e -target -value)
                          key-code (.. e -keyCode)]
                      (when
                          (and
                           (= key-code 13)
                           (not (clojure.string/blank? text)))
                        (do
                          (.preventDefault e)
                          (om/transact! this `[(command/add {:cmd ~(cljs-read-eval-print! text)}) :command/list])
                          (set! (.. e -target -value) "")))))
  (render [this]
          (let [{:keys [app/title command/list] :as props} (om/props this)]
            (styx-template this list props))))


(om/add-root!
 reconciler
 Styx
 (gdom/getElement "app"))
