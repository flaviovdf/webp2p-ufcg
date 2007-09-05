set terminal postscript enhanced eps color 20
set out 'big_chart_projeto1.eps'

set key left
set xtics 1
set yrange [0:1.3]
set xrange [0:6]
set style fill solid

set encoding iso_8859_1

set boxwidth 0.8

pesous = 1.0/5

plot 'projeto1.data' w boxes t 'Evolução' lw 1, (pesous * 5) t 'Relatório Final e Simulador' lw 6, (pesous * 3) t 'Relatório V2' lw 6, (pesous * 2) t 'Relatório V1' lw 6
