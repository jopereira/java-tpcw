function [n] = ishort(i)
%-------------------------------------------------------------------------
% function [n] = ishort(i)
%
% Returns the four letter string name of an interaction.
%-------------------------------------------------------------------------

n = { 'init', 'admc', 'admr', 'bess', 'buyc', 'buyr', 'creg', 'home', 'newp', 'ordd', 'ordi', 'prod', 'sreq', 'sres', 'shop' };

n = n{i};

