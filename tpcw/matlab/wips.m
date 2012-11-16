function [w] = wips(dat)
%------------------------------------------------------------------------
% function [] = wips(dat)
%
% Plot throughput (as web-interactions per second, WIPS) over time.
% Compute and plot average throughput over measurement interval.
% Plot +/- 5% bounds.
%------------------------------------------------------------------------
global wipsNorm

% Find end of data.

for i=1:length(dat.wips)
  j = length(dat.wips)-i+1;
  if (dat.wips(j)>0) 
    break;
  end
end

len = j;

clf;
hold on;
l = plot(dat.wips(1:len)/wipsNorm, 'r.');

avg(1) = 0;
for i=1:30
  avg(1) = avg(1)  +dat.wips(i);
end

for i=31:len
  avg(i-29) = avg(i-30) + dat.wips(i) - dat.wips(i-30);
end

avg = avg / 30;

l = plot((30:len)-15, avg/wipsNorm, 'k-');

ax = axis;
s = (dat.startMI-dat.startRU)/1000;
e = (dat.startRD-dat.startRU)/1000;
plot([s,s], ax(3:4), 'b--');
plot([e,e], ax(3:4), 'b--');

s=floor(s);
e=ceil(e);

avg = sum(dat.wips(s:e))/(e-s+1)/wipsNorm;
plot([s,e], [avg, avg], 'g--');

%title({'Throughput Over Time', sprintf('Avg WIPS = %9.2f', avg)});
title('Throughput Over Time');
ylabel('Normalized WIPS');
xlabel('Time (s)');
