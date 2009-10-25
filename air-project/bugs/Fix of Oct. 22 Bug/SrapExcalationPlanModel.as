/**
 * Copyright (c) Quintiles Transnational Corp. or its subsidiaries.
 * All Rights Reserved.
 */
package com.quintiles.views.components.reportQuestions.model {

    public class SrapExcalationPlanModel {
        [Bindable]
        public var date:String;
        [Bindable]
        public var step:String;
        [Bindable]
        public var other:String;

        public var questions:Array;
        
        public function SrapExcalationPlanModel() {
        }
    }
}