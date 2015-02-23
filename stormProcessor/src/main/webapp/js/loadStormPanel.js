var _intervalsArray = [];
var instrumentSymbol = " ";
var sma = [], ema = [];
var dataset;
var updateInterval = 500;
var now = new Date().getTime();
var day, hours;
var curentDate, counter = 0;
var indicatorsValues = new Array();
var i = 0;
var g = 0;
var isFirstValuesToPlot = true;
var choosenInstrumentsCounter = 0;
var hourCheck = true, dayCheck = true, dayFlag = true;
tooltipFlag = true, windowFlag = true;
var minY;
var flag = true;
var $plot = null;

/**
 * Body runs when DOM is ready
 */
$(document).ready(function() {

	/*
	 * Filling list of supported instruments
	 */
	var $instrumentModal = $("#instrumentsModal");
	var $instrumentTable = $("#instrumentsTable");
	getAndShowInstruments($instrumentModal, $instrumentTable);

	/*
	 * Adding tooltips
	 */
	$('#sma-acro').tooltip({
		animation : true,
		placement : 'left',
		title : 'Simple Moving Average'
	});

	$('#ema-acro').tooltip({
		animation : true,
		placement : 'left',
		title : 'Exponential Moving Average'
	});

	/*
	 * Adding scroll bar to indicators table
	 */
	$("#table-container").slimScroll({
		height : '350px'
	});

});

/**
 * Function sends ajax request REST service for supported instruments and put
 * them into a list
 * 
 * @param $modal
 *            List where You want to put instruments
 * @param $table
 */
function getAndShowInstruments($modal, $table) {
	$.getJSON('http://PLD0080:8880/storm-processor/service/instruments',
			function(data) {
				showInstruments($modal, data);
				bindInstrumentsClickEvents($modal, $table);
			});
}

/**
 * Function writes into list nodes that store instruments links
 * 
 * @param $modal
 *            List where You want to put instruments
 * @param instruments
 *            Array of instruments
 */
function showInstruments($modal, instruments) {
	var $groupList = $modal.find(".list-group");
	$.each(instruments, function(key) {
		var instrumentName = instruments[key];
		addInstrumentToModalList("list-group-item", instrumentName,
				instrumentName, $groupList);
	});
}

/**
 * Helper function adds link element to modal list of instruments
 * 
 * @param className
 *            Class name for link
 * @param dataValue
 *            data-value attribute value
 * @param text
 *            Text for link
 * @param $parent
 *            Append link element to this parent element
 */
function addInstrumentToModalList(className, dataValue, text, $parent) {
	var $item = $('<a href="#">');
	$item.addClass(className);
	$item.attr("data-value", dataValue);
	$item.text(text);
	$parent.append($item);
}

/**
 * Function creates table row for an instrument
 * 
 * @param instrument
 *            Name of an instrument
 * @param $table
 *            Table where You want to append an instrument row
 */
function addInstrumentToTable(instrument, $table) {
	var $tr = $("<tr>");
	$tr.addClass("instrument");
	$tr.attr("data-value", instrument);

	// Name column
	var $nameColumn = createColumnForIndicatorsTable("instrument-name", null);
	var $inputName = createInputForIndicatorTable(instrument);
	$inputName.appendTo($nameColumn);

	// SMA value column
	var $valueSmaColumn = createColumnForIndicatorsTable(
			"instrument-sma-value", null);
	var $inputSmaValue = createInputForIndicatorTable("0.0");
	$inputSmaValue.appendTo($valueSmaColumn);

	// EMA value column
	var $valueEmaColumn = createColumnForIndicatorsTable(
			"instrument-ema-value", null);
	var $inputEmaValue = createInputForIndicatorTable("0.0");
	$inputEmaValue.appendTo($valueEmaColumn);

	// Adding delete button
	var $deleteButton = createDeleteButton("delete-instrument", instrument,
			deleteInstrumentButtonFun);
	var $delBtnColumn = createColumnForIndicatorsTable("", {
		"text-align" : "center"
	});
	$delBtnColumn.append($deleteButton);

	// Appending all table structure
	$tr.append($nameColumn);
	$tr.append($valueSmaColumn);
	$tr.append($valueEmaColumn);
	$tr.append($delBtnColumn);

	$table.append($tr);
}

/**
 * Helper function creates table column with specific class name and css object
 * 
 * @param className
 *            Class name
 * @param css
 *            Css object
 * @returns {*|jQuery|HTMLElement} Column node
 */
function createColumnForIndicatorsTable(className, css) {
	var $column = $('<td>');
	$column.addClass(className);

	if (css != null) {
		$column.css(css);
	}

	return $column;
}

/**
 * Helper function creates input element with specific value
 * 
 * @param value
 *            Input value
 * @returns {*|jQuery|HTMLElement} Input element
 */
function createInputForIndicatorTable(value) {
	var $input = $('<input type="text" class="form-control" readonly />');
	$input.val(value);
	return $input;
}

/**
 * Helper function creates delete button
 * 
 * @param className
 *            Button class name
 * @param dataValue
 *            data-value attribute value
 * @param onClickFunction
 *            function invoked after clicking button
 * @returns {*|jQuery|HTMLElement} HTML button element
 */
function createDeleteButton(className, dataValue, onClickFunction) {
	var $button = $('<button class="btn btn-default">Remove</button>');
	$button.addClass(className);
	$button.attr("data-value", dataValue);
	$button.on('click', onClickFunction);
	return $button;
}

/**
 * Function adds click events into instrument nodes in html list
 * 
 * @param $modal
 *            Modal window with a HTML list
 * @param $table
 *            HTML table where You want to store your instruments
 */
function bindInstrumentsClickEvents($modal, $table) {
	var $items = $modal.find(".list-group-item");

	$items.on('click', function() {

		var instrumentName = $(this).attr("data-value");

		if (!isInstrumentOnTable(instrumentName, $table)) {
			addInstrumentToTable(instrumentName, $table);
			setIndicatorInterval(instrumentName, "SIMPLE_MOVING_AVERAGE");
			addOrRemoveActiveClassInInstrumentModal($(this), 'ADD');
		} else {
			var $notificationPlaceholder = $("#instrumentsModal").find(
					".notification-placeholder");
			sendNotification($notificationPlaceholder,
					"<span class='glyphicon glyphicon-star'></span> Instrument <strong>"
							+ instrumentName
							+ "</strong> is already in the table.", "info",
					2000);
		}

		updateInstrumentsList($("#instruments-list"), $table);
		showInfoIfTableIsEmpty($("#table-empty-info"), $table, "instrument");

	});
}

/**
 * Function checks if instrument is already int the table
 * 
 * @param instrumentName
 *            Name of an instrument
 * @param $table
 *            Table where You want to scan
 * @returns {boolean} If true - instrument is in the table, false - not
 */
function isInstrumentOnTable(instrumentName, $table) {
	var answer = false;

	$table.find(".instrument").each(
			function() {

				if (instrumentName == $(this).attr("data-value")) {
					console.log("Instrument that You want to add: "
							+ instrumentName + ". We have it in table: "
							+ $(this).attr("data-value"));
					answer = true;
					return false;
				} else {
					return true;
				}

			});

	return answer;
}

/**
 * Function deletes instrument row
 */
function deleteInstrumentButtonFun() {
	var $button = $(this);
	var $instrumentsTable = $("#instrumentsTable");

	console.log("Deleting instrument row for: " + $(this).attr("data-value"));
	if ($(this).attr("data-value") == instrumentSymbol) {
		sma = [];
		ema = [];
		g = 0;
		isFirstValuesToPlot = true;
		dataset = [];
		flag = true;
		dayCheck = true;
	}

	$button.closest("tr.instrument").remove();

	clearIndicatorInterval($(this).attr("data-value"));

	updateInstrumentsList($("#instruments-list"), $instrumentsTable);
	showInfoIfTableIsEmpty($("#table-empty-info"), $instrumentsTable,
			"instrument");

	var $item = $('.list-group-item[data-value="' + $(this).attr('data-value')
			+ '"]');
	addOrRemoveActiveClassInInstrumentModal($item, 'DEL');

}

/**
 * Function sets intervals for specific instrument and indicator type
 * 
 * @param instrumentName
 *            Name of instrument
 * @param indicatorType
 *            Type of indicator
 */
function setIndicatorInterval(instrumentName, indicatorType) {
	if (_intervalsArray[instrumentName] == null) {
		_intervalsArray[instrumentName] = [];
	}

	_intervalsArray[instrumentName][indicatorType] = window.setInterval(
			function() {
				getIndicator(instrumentName, "SIMPLE_MOVING_AVERAGE");

			}, 2000);
}

/**
 * Function clears intervals for a specific instrument
 * 
 * @param instrumentName
 *            Name of instrument
 */
function clearIndicatorInterval(instrumentName) {
	for ( var key in _intervalsArray[instrumentName]) {
		if (_intervalsArray[instrumentName].hasOwnProperty(key)) {
			window.clearInterval(_intervalsArray[instrumentName][key]);
			_intervalsArray[instrumentName][key] = null;
		}
	}
}

function clearPlot() {
	instrumentSymbol = (this.textContent === undefined) ? this.innerText
			: this.textContent;
	sma = [];
	ema = [];
	g = 0;
	isFirstValuesToPlot = true;
	dataset = [];

	var instrumentName = $(this).attr("data-value");
	highlightCurrentInstrumentInTable(instrumentName);

	return false, flag = true, dayCheck = true;
}

/**
 * Function highlights current active instrument
 * 
 * @param instrumentName
 *            Name of an instrument
 */
function highlightCurrentInstrumentInTable(instrumentName) {
	var $instrumentsRows = $("#instrumentsTable").find(".instrument");
	$instrumentsRows.removeClass("active");
	$instrumentsRows.each(function() {
		if ($(this).attr("data-value") == instrumentName) {
			$(this).addClass("active");
		}
	});
}

/**
 * Function gets indicator value using AJAX and puts it to a table field
 * 
 * @param instrumentName
 *            Name of instrument that You want to get value
 * @param indicatorType
 *            Type of indicator
 */
function getIndicator(instrumentName, indicatorType) {

	switch (indicatorType) {
	case "SIMPLE_MOVING_AVERAGE":
		$
				.getJSON(
						'http://PLD0080:8880/storm-processor/service/simple-moving-average',
						{
							symbol : instrumentName
						},
						function(json) {
							getIndicator(instrumentName,
									"EXPONENTIAL_MOVING_AVERAGE");
							$("#instrumentsTable")
									.find(
											"tr[data-value='"
													+ json.symbol
													+ "'] td.instrument-sma-value input")
									.val(json.value);
						});
		break;
	case "EXPONENTIAL_MOVING_AVERAGE":
		$
				.getJSON(
						'http://PLD0080:8880/storm-processor/service/exponential-moving-average',
						{
							symbol : instrumentName
						},
						function(json) {
							$("#instrumentsTable")
									.find(
											"tr[data-value='"
													+ json.symbol
													+ "'] td.instrument-ema-value input")
									.val(json.value);

							var d = json.published.split("-");
							var t = json.time.split(":");
							var nowD = new Date(d[0], d[1], d[2], t[0], t[1],
									t[2], 0);
							now = nowD.getTime();

							console.log("Date from JSON:  " + json.published);
							console.log("Parsed date:  " + nowD.getFullYear()
									+ "/" + nowD.getMonth() + "/"
									+ nowD.getDate());

							if (instrumentSymbol == json.symbol) {

								uptd();
								$('#dateOfData').val(json.published);
								curentDate = now;
							}

							
						});
		break;
	}

}
function initData() {
	now -= (3600 * 4000);
	for ( var i = 0; i < 3600; i++) {
		var temp = [ now += 4000, 0 ];
		sma.push(temp);
		ema.push(temp);
	}
	$("#placeholder").bind(
			"plothover",
			function(event, pos, item) {

				if (item) {

					var y = item.datapoint[1].toFixed(2);
					var date = new Date(item.datapoint[0]);
					var minutes = "";
					if (date.getMinutes() <= 9) {
						minutes = "0" + date.getMinutes();
					} else {
						minutes = date.getMinutes();
					}
					var dateInFormat = "";
					if (date.getMonth() == 0) {
						dateInFormat = date.getHours() + ":" + minutes + " "
								+ (date.getFullYear() - 1) + "-" + "12" + "-"
								+ date.getDate();
					} else {
						dateInFormat = date.getHours() + ":" + minutes + " "
								+ (date.getFullYear() - 1) + "-"
								+ date.getMonth() + "-" + date.getDate();
					}
					if (item.series.label == "EMA") {
						$("#tooltip2").hide();
						$("#tooltip").html(
								"<center>" + dateInFormat + "<br>" + y
										+ "</center>").css({
							top : item.pageY + 5,
							left : item.pageX + 5
						}).fadeIn(200);
					} else if (item.series.label == "SMA") {
						$("#tooltip").hide();
						$("#tooltip2").html(
								"<center>" + dateInFormat + "<br>" + y
										+ "</center>").css({
							top : item.pageY + 5,
							left : item.pageX + 5
						}).fadeIn(200);
					}
				} else {
					$("#tooltip").hide();
					$("#tooltip2").hide();
				}

			});
};

/**
 * Function updates list of instruments that are in the table
 * 
 * @param $instrumentList
 *            HTML list element of instruments
 * @param $checkedTable
 *            HTML table element
 */
function updateInstrumentsList($instrumentList, $checkedTable) {

	$instrumentList.find("li").remove();
	choosenInstrumentsCounter = 0;
	$.each($checkedTable.find(".instrument"), function() {
		addInstrumentNodeToList($instrumentList, $(this).attr("data-value"));
	});

}
var temp;

/**
 * Function sets value of minimum Yaxis depending on indicators Values
 * 
 * @returns minY minimum value of Yaxis
 */
function SetYaxisMin() {
	if (indicatorsValues['ema'] >= indicatorsValues['sma']) {
		return minY = indicatorsValues['sma'] - 0.15;
	} else {
		return minY = indicatorsValues['ema'] - 0.1;
	}
}

/**
 * Function updates value of minimum Yaxis depending on indicators Values
 * 
 * @returns minY minimum value of Yaxis
 */
function GetYaxisMinFromindicatorsValues() {

	if ((minY + 0.02) >= indicatorsValues['ema']
			&& (minY + 0.02) >= indicatorsValues['sma']) {

		SetYaxisMin();
		// return minY;
	}
	console.log(minY);
	return minY;
}

/**
 * Function set value of currnet day from json
 * 
 * @returns day
 */

function setDate() {
	var date = new Date(now);

	return day = date.getDate(), hours = date.getHours();

}

/**
 * Function check if date from json is the same as currnet date on chart
 * 
 * @returns dayFlag boolean value for cleaning chart
 */
function checkIfDateOnChartIsProperly() {
	var nDay = new Date(now);

	if (nDay.getDate() == day && nDay.getHours() >= hours) {
		console.log(nDay.getDate() + " " + nDay.getHours() + ":"
				+ nDay.getMinutes());
		return dayFlag = true;
	} else {

		return dayFlag = false;

	}

}

function uptd() {
	$('#instrument').val(instrumentSymbol);
	indicatorsValues['sma'] = $("#instrumentsTable").find(
			"tr[data-value='" + instrumentSymbol
					+ "'] td.instrument-sma-value input").val();
	indicatorsValues['ema'] = $("#instrumentsTable").find(
			"tr[data-value='" + instrumentSymbol
					+ "'] td.instrument-ema-value input").val();

	if (now == curentDate) {
		if (windowFlag == true) {
			counter++;
		}

		console.log(counter);
		if (counter == 4) {
			// BootstrapDialog.alert('No new data to display on chart. Please
			// try again later.');
			var dialog = new BootstrapDialog(
					{
						title : 'Information.',
						message : 'No new data to display on chart. Please try again later.',
						buttons : [ {
							label : 'Close',
							cssClass : 'btn-info',
							action : function() {
								BootstrapDialog.closeAll();
								windowFlag = true;
							}
						} ]
					});
			dialog.open();
			windowFlag = false;
			counter++;
		}
		if (counter > 4) {
			counter = 0;
		}

	} else {
		counter = 0;
		console.log(curentDate);
		console.log(now);
		if (isFirstValuesToPlot == true) {
			initData();
			isFirstValuesToPlot = false;
		}

		if (g < 36) {
			for ( var o = 0; o < 100; o++) {
				sma.shift();
				ema.shift();
			}
			g++;
		}

		temp = [ now, indicatorsValues['sma'] ];
		sma.push(temp);
		temp = [ now, indicatorsValues['ema'] ];
		ema.push(temp);

		dataset = [ {
			label : "SMA",
			data : sma,
			lines : {
				fill : false,
				lineWidth : 1.2
			},
			color : "#00FF00"
		}, {
			label : "EMA",
			data : ema,
			lines : {
				fill : false,
				lineWidth : 1.2
			},
			color : "#000000"
		} ];

		if (flag == true) {
			SetYaxisMin();
			flag = false;
		}

		if (dayCheck == true) {
			setDate();
			console.log(day + ":" + hours);
			dayCheck = false;
		}

		GetYaxisMinFromindicatorsValues();
		checkIfDateOnChartIsProperly();
		var options = {
			series : {
				lines : {
					show : true,
					lineWidth : 1.2
				},
				points : {
					show : true
				},
				bars : {
					align : "center",
					fillColor : {
						colors : [ {
							opacity : 1
						}, {
							opacity : 1
						} ]
					},
					barWidth : 1800,
					lineWidth : 1
				}
			},
			xaxis : {
				mode : "time",
				tickSize : [ 3600, "second" ],
				tickFormatter : function(v, axis) {
					var date = new Date(v);
					console.log("Date in tickFormatter:  " + date.getFullYear()
							+ "/" + (date.getMonth() - 1) + "/"
							+ date.getDate());

					if (date.getSeconds() % 6000 == 0) {
						var hours = date.getHours() < 10 ? "0"
								+ date.getHours() : date.getHours();
						var minutes = date.getMinutes() < 10 ? "0"
								+ date.getMinutes() : date.getMinutes();

						return hours + ":" + minutes;
					} else {
						return "";
					}
				},
				axisLabel : "Time",
				axisLabelUseCanvas : true,
				axisLabelFontSizePixels : 12,
				axisLabelFontFamily : 'Verdana, Arial',
				axisLabelPadding : 10
			},
			yaxes : [ {
				min : minY,

				// tickSize: 0.04,

				axisLabel : "Indicators value",
				axisLabelUseCanvas : true,
				axisLabelFontSizePixels : 12,
				axisLabelFontFamily : 'Verdana, Arial',
				axisLabelPadding : 6
			}, {
				max : 5120,
				position : "left",
				axisLabel : "Disk",
				axisLabelUseCanvas : true,
				axisLabelFontSizePixels : 12,
				axisLabelFontFamily : 'Verdana, Arial',
				axisLabelPadding : 6
			} ],
			legend : {
				noColumns : 0,
				position : "nw"
			},

			grid : {
				hoverable : true,
				backgroundColor : {
					colors : [ "#ffffff", "#EDF5FF" ]
				}
			}
		};

		if (dayFlag == true) {

			$plot = $.plot($("#placeholder"), dataset, options);
		} else {
			clearPlot();
			instrumentSymbol = $('#instrument').val();
			highlightCurrentInstrumentInTable(instrumentSymbol);
		}

	}
}

$("<div id='tooltip'></div>").css({
	position : "absolute",
	display : "none",
	border : "5px solid #174171",
	padding : "12px",
	"background-color" : "#f1f1f1",
	"font-size" : "15px",
	opacity : 100.80
}).appendTo("body");

$("<div id='tooltip2'></div>").css({
	position : "absolute",
	display : "none",
	border : "5px solid #00FF00",
	padding : "12px",
	"background-color" : "#f1f1f1",
	"font-size" : "15px",
	opacity : 100.80
}).appendTo("body");

/**
 * Function adds single instrument node into instruments list
 * 
 * @param $instrumentList
 *            List of instruments
 * @param instrumentName
 *            Name of instrument
 */
function addInstrumentNodeToList($instrumentList, instrumentName) {
	var $li = $("<li>");
	$li.attr("data-value", instrumentName);
	$li.append($("<a href='#'>" + instrumentName + "</a>"));
	$instrumentList.append($li);
	var lis = document.getElementById("instruments-list").getElementsByTagName(
			'li');
	for ( var h = 0; h < lis.length; h++) {
		lis[h].addEventListener('click', clearPlot, true);
	}
	choosenInstrumentsCounter++;
	if (choosenInstrumentsCounter == 1) {
		instrumentSymbol = instrumentName;
		highlightCurrentInstrumentInTable(instrumentName);
	}

}

/**
 * Function adds/removes class 'active' to some item node
 * 
 * @param $item
 *            HTML element
 * @param action
 *            What You want to do: ADD=add class, DEL=remove class
 */
function addOrRemoveActiveClassInInstrumentModal($item, action) {
	if (action == 'ADD') {
		$item.addClass('active');
	} else if (action == 'DEL') {
		$item.removeClass('active');
	} else {
		console.log("Action attribute incorrect!");
	}
}
