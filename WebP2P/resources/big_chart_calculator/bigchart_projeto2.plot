set terminal postscript enhanced eps color 20
set out 'big_chart_projeto2.eps'

set key left
set xtics 1
set yrange [0:1.5]
set xrange [0:7]
set style fill solid

set encoding iso_8859_1

set boxwidth 0.8

pesous = 1.0/6

plot 'projeto2.data' w boxes t 'Evolução' lw 1, (pesous * 6) t 'Entrega Final' lw 6, (pesous * 5) t 'Web Server' lw 6, (pesous * 4) t 'Discovery Service' lw 6, (pesous * 3) t 'Proxy' lw 6, (pesous * 1) t 'Simulador' lw 6
