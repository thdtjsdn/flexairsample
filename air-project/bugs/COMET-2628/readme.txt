It is a bug not easy to fix. http://10.36.205.184/jira/browse/COMET-2628

Related code:
IssuesAndActionsView.mxml
DateRangeFilter.mxml

See the attached two version codes.

The original problem is caused by data binding unconsistence status. In order to make status reasonable, we need to use .as code to change status step by step. Several rules:
1) When initailization, two dates are forwarded from outside, so we need to insert codes into property setter API. If date is assigned because of user operation, the setter API is also be invoked (to keep the property with the same value of DateChooser status), at that time, we just keep the value, don't do any special check.
    If it is the first case which value is coming from outside, we also need to reset other date range. For example, when set rangeStartSelectedDate, we will set it as the start point of end DateChooser, which is the restriction of UI operation (Users can't select a date before rangeStartSelectedDate in end date DateChooser).

2) When user operate DateChooser, we will keep the status and also reset the range of other DateChooser.

After that, the user operation is simple, and no status confusion happens.

Thanks.

Kui