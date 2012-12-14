function [s] = statProb(t)
%-------------------------------------------------------------------------
% function [s] = statProb(t)
%
% Finds the steady-state probabilites for a state transition table.
% Assumes the original transition table, t, is ergodic.
%-------------------------------------------------------------------------
s = [1,zeros(1,14)];

for i=1:29
  s = s*t;
end

