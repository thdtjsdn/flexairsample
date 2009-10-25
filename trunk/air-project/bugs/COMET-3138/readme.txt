Hi,

 The bug is fixed. http://10.36.205.184/jira/browse/COMET-3138. It is a difficult bug, not easy to fix.

Related code:
Class:    com.quintiles.utils.ReportQuestionValidatorUtil.as 
Method: validateRadioGroupHorizontal()
Line:      291

Original Code:

        public static function validateRadioGroupHorizontal(question:ReportQuestionVO):Boolean {
            if (!isStringNotEmpty(question.questionAnswer))
                return false;

            var answerArray:Array = question.questionAnswer.split(SqlUtils.VALUE_DELIMITER);
            var selectedIndex:String = findQuestionSelectedIndex(question.questionValues, answerArray[0]);

            if (question.questionAddInfoField && additionalQuestionTriggered(selectedIndex, question.questionAddInfoField)) {
                return (answerArray[1] && answerArray[1].toString().length > 0);
            }

            return (answerArray[0].toString().length > 0);
        }

Changed Code:

        public static function validateRadioGroupHorizontal(question:ReportQuestionVO):Boolean {
            if (!isStringNotEmpty(question.questionAnswer))
                return false;

            var answerArray:Array = question.questionAnswer.split(SqlUtils.VALUE_DELIMITER);
            var selectedIndex:String = findQuestionSelectedIndex(question.questionValues, answerArray[0]);
            // check if current answer needs extra info field
            if (question.questionAddInfoField && additionalQuestionTriggered(selectedIndex, question.questionAddInfoField)) {
                // the extra info maybe come from question.answers[1], especially no user fill, from original status
                if(question.answers.length > 1) {
                    var secondenswer:String = (question.answers[1] as ReportAnswerVO).questionAnswer;
                    return (secondenswer && secondenswer.length > 0);
                 } else {
                     return (answerArray[1] && answerArray[1].toString().length > 0);
                 }
            }

            return (answerArray[0].toString().length > 0);
        }

Analytics:
   When you select "Yes" and then fill content "hello" on TextArea, the value of "question.questionAnswer" will be "Yes|hello", so the original logic will work. The key point is to validate the extra info (here it is "hello") when the answer need it (through "questionAddInfoField").
   When you select "No" firstly and then switch to "Yes", the value of "question.questionAnswer" will be "Yes", the extra info is not attached here. Actually extra info will be always in "question.answers[1]". When the answer need extra info, it always have two elements at "answers" field.
   So i change the logic as above.

Thanks.