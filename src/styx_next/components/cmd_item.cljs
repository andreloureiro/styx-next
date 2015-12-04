(ns styx-next.components.cmd-item
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :refer [li small pre]]
            [replumb.core :as replumb]))

(defn cmd-item-template
  [success? form value error props]
  (let [item-class
        (if success? "history__item--success" "history__item--error")
        large-value (if (< 20 (count value)) "item__value--large" "")]
    (li #js {:className (str "history__item " item-class)}
        (small #js {:className "item__form"} (str form))
        (if success?
          (pre #js {:className (str "item__value " large-value)} value)
          (pre #js {:className "item__value"} error)))))

(defui CmdItem
  static om/Ident
  (ident [this props]
         `[:command/by-id ~(:command/id props)])

  static om/IQuery
  (query [this]
         '[:command/id :command/success? :command/form :command/value])

  Object
  (render [this]
          (let [{:keys [command/success? command/form command/value
                        command/error] :as props} (om/props this)
                prompt (replumb/get-prompt)]
            (cmd-item-template success? form value error props))))

(def cmd-item (om/factory CmdItem))

