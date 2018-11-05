$(function () {
function initPage(){                                                  
    Highcharts.setOptions({                                                     
        global: {                                                               
            useUTC: false                                                       
        }                                                                       
    });                                                                         
                                                                                
    var chart;   
    var data =1000+200*Math.random();                                                          
    $('#container').highcharts({                                                
        chart: {                                                                
            type: 'spline',                                                     
            animation: Highcharts.svg, // don't animate in old IE               
            marginRight: 10,                                                    
            events: {                                                           
                load: function() {                                              
                                                                                
                    // set up the updating of the chart each second             
                    var series = this.series[0];                                
                    setInterval(function() {                                    
                        var x = (new Date()).getTime(), // current time         
                            y = data;                               
                        series.addPoint([x, y], true, true);                    
                    }, 5000);                                                   
                }                                                               
            }                                                                   
        },                                                                      
        title: {                                                                
            text: '上联口性能统计图'                                            
        },                                                                      
        xAxis: {                                                                
            type: 'datetime',                                                   
            tickPixelInterval: 100                                              
        },                                                                      
        yAxis: {                                                                
            title: {                                                            
                text: '--单位(kb)'                                                   
            },                                                                  
            plotLines: [{                                                       
                value: 0,                                                       
                width: 1,                                                       
                color: '#808080'                                                
            }]                                                                  
        },                                                                      
        tooltip: {                                                              
            formatter: function() {                                             
                    return '<b>'+ this.series.name +'</b><br/>'+                
                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br/>'+
                    Highcharts.numberFormat(this.y, 2);                         
            }                                                                   
        },                                                                      
        legend: {                                                               
            enabled: false                                                      
        },                                                                      
        exporting: {                                                            
            enabled: false                                                      
        },
        //初始数据                                                                      
        series: [{                                                              
            name: '瞬时数据',                                                
            data: (function() {                                                 
                // generate an array of random data                             
                var data = [],                                                  
                    time = (new Date()).getTime(),                              
                    i;                                                          
                                                                                
                for (i = -10; i <= 0; i++) {                                    
                    data.push({                                                 
                        x: time + i * 5000,                                     
                        y: 123                                   
                    });                                                         
                }                                                               
                return data;                                                    
            })()                                                                
        }]                                                                      
    });                                                                         
}                                                                             
initPage();                                                                               
});