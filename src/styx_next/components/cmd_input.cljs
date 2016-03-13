(ns styx-next.components.cmd-input
  (:require [om.dom :refer [div p input]]
            [om.next :as om :refer-macros [defui]]
            [replumb.core :as replumb]))

(defn cmd-input-template [handle-input prompt active]
  (div #js {:className "styx__input-area"}
       (p #js {:className "input-area__prompt"} prompt)
       (input #js {:className "input-area__input"
                   :onKeyDown   handle-input
                   :ref "styxInput"})))

(defui CmdInput
  Object
  (componentDidMount [this]
                     (println (.-styxInput (.-refs this))))

  (render [this]
          (let [{:keys [history/active] :as props} (om/props this)
                {:keys [handle-input] :as computed} (om/get-computed this)
                prompt (replumb/get-prompt)]
            (println props)
            (cmd-input-template handle-input prompt active))))

(def cmd-input (om/factory CmdInput))

