(ns styx-next.components.templates)

(defn cmd-input-template [handle-input prompt]
  (div #js {:className "styx__input-area"}
       (p #js {:className "input-area__prompt"} prompt)
       (input #js {:className "input-area__input"
                   :placeholder "your command, master!"
                   :onKeyDown   handle-input})))

(defn cmd-item-template
  [success? form value error props]
  (let [item-class
        (if success? "history__item--success" "history__item--error")
        large-value (if (< 20 (count value)) "item__value--large" "")]
    (li #js {:className (str "history__item " item-class)}
        (small #js {:className "item__form"} (str form))
        (println value)
        (if success?
          (pre #js {:className (str "item__value " large-value)} value)
          (pre #js {:className "item__value"} error)))))

(defn command-history-template
  [history]
  (apply ul #js {:className "styx__history"} (map styx-cmd-item history)))

(defn styx-template
  [this title list props]
  (div #js {:className "styx"}
       (h3 #js {:className "styx__title"} title)
       (styx-history list)
       (styx-cmd-input
        (om/computed props {:handle-input #(.handle-cmd-input this %)}))))
