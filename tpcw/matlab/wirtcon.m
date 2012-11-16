function [n] = wirtcon(i)
%-------------------------------------------------------------------------
% function [n] = wirtcon(i)
%
% Returns the 90% WIRT contraint for a given interaction number.
%-------------------------------------------------------------------------

% Official TPC-W Spec
% n = [1 20 1 5 5 1 2 1 5 2 1 1 1 10 1];

% Ours.
n = [5 20 5 5 5 5 2 5 5 2 5 5 5 10 5];

n = n(i);

