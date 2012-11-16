function [w] = wirt(dat)
%------------------------------------------------------------------------
% function [] = wirt(dat)
%
% Plots web interaction response time (WIRT) for all transactions.
% These are plotted in two graphs, as defined by the TPC-W Spec.
% An inverse cumulative distribution is used, which indicates
%  what percentage of the interactions took longer than a given
%  time.
%------------------------------------------------------------------------

clf;
subplot(2,1,1);
wirti(dat, [wi_home, wi_prod, wi_sreq, wi_shop, wi_buyr, wi_ordi, wi_admr]);

subplot(2,1,2);
wirti(dat, [wi_newp, wi_bess, wi_sres, wi_creg, wi_buyc, wi_ordd, wi_admc]);

figure(gcf);
