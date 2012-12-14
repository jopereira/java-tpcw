function [w,t] = wirtvsthr(d)
%-------------------------------------------------------------------------
% function [] = wirtvsthr(d)
%
% WIRT vs. Througput.
%
% D is a {} of run structures, for different numbers of EBs.
%-------------------------------------------------------------------------

global wirtNorm
global wipsNorm

j = [];
w = [];
t = [];
for i=1:length(d)
  if (~isempty(d{i})) 
    j = [j, i];
    w = [w, d{i}.wirt{wi_home}.avg];
    s = ceil((d{i}.startMI-d{i}.startRU)/1000);
    e = floor((d{i}.startRD - d{i}.startRU)/1000);
    t = [t, sum(d{i}.wips(s:e))/(e-s)];
  end
end

%subplot(1,2,2);
%plot(t, w/1000, 'k-o');
%xlabel('Home Response Time (s)');
%ylabel('Throughput (WIPS)');
subplot(2,1,1);
plot(j, w/1000, 'k-o');
xlabel('Emulated Browsers');
ylabel('Norm. Home Int. Resp. Time (s)');
grid on;
subplot(2,1,2);
plot(j, t/wipsNorm, 'k-o');
xlabel('Emulated Browsers');
ylabel('Norm. Throughput (WIPS)');
grid on;
