var ylt= ylt ||{};
ylt.Chart = ylt.Chart || {};
var yltChart=ylt.Chart={
		generPieData:function(_strTitle,_strSecTitle,_arrLegend,_arrData,_bIShowLegend){
				var iDataLength=_arrData.length;
				var arrChartData=new Array(iDataLength);
				for(var i=0;i<iDataLength;i++){
					arrChartData[i]={value:_arrData[i], name:_arrLegend[i]};
				}
				var option = {
								title : {
											text: _strTitle,
											subtext: _strSecTitle,
											x:'center'
										},
								tooltip : {
											trigger: 'item',
											formatter: "{a} <br/>{b} : {c} ({d}%)"
											},
								legend: {
											orient: 'vertical',
											left: 'right',
											data: _arrLegend
										},
								series : [
											{
												type: 'pie',
												radius : '55%',
												center: ['50%', '60%'],
												data:arrChartData,
												itemStyle: {
																emphasis: {
																			shadowBlur: 10,
																			shadowOffsetX: 0,
																			shadowColor: 'rgba(0, 0, 0, 0.5)'
																			}
															}
											}
										]
							};
							if(!_bIShowLegend)
								option.legend={};
							return option;
	},
	generBarData:function(_strTitle,_strSecTitle,_arrColors,_strGraphType,_arrLegend,_arrXAxis,_arrData,_arrStyle){
		var iDataLength=_arrLegend.length;
		var arrSeries=new Array(iDataLength);
		for(var i=0;i<iDataLength;i++){
			arrSeries[i]={
							name:_arrLegend[i],
							type:_strGraphType,
							//stack: '����',
							itemStyle : { normal: {label : {show: false, position: 'insideRight'}}},
	
							data:_arrData[i]
						}
		}
		
		var option = {
						color:_arrColors,
						backgroundColor:'white',						
						tooltip : {
									trigger: 'axis',
									axisPointer : {            // ������ָʾ���������ᴥ����Ч
													type : 'line'        // Ĭ��Ϊֱ�ߣ���ѡΪ��'line' | 'shadow'
									}
						},
						legend: {
								data:_arrLegend
						},
						grid: {
								left: '3%',
								right: '4%',
								bottom: '3%',
								containLabel: true
							},
						xAxis : [
									{
										type : 'category',
										data : _arrXAxis
									}
								],
						yAxis : [
									{
										type : 'value'
									}
								],
						series : arrSeries
					};
		return option;
	}
}