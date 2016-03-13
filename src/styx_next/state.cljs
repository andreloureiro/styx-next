(ns styx-next.state
  (:require [styx-next.utils :refer [guid]]))

(def initial-state {:app/title    "styx"
                    :command/list []
                    :history/active {:active/index nil
                                     :active/form nil}})
