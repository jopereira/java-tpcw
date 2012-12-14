function [w] = think(dat)
%------------------------------------------------------------------------
% function [] = think(dat)
%
% Plots a distribution of think time over all transactions.
%------------------------------------------------------------------------

clf;
hold on;
l = plot(dat.tt.h(:,1)/1000, dat.tt.h(:,2));

title({'Think Time Histogram', sprintf('Avg = %9.2f', dat.tt.avg/1000)});
ylabel('Interactions');
xlabel('Think Time (s)');
