function [] = printwi(dat)
%-------------------------------------------------------------------------
% function [] = printwi(dat)
%
% Print a vector of values (dat) in order of TPC-W Spec. 5.2.1.
% Useful for looking at mixes.
%-------------------------------------------------------------------------

i1 = [wi_home, wi_newp, wi_bess, wi_prod, wi_sreq, wi_sres];
i2 = [wi_shop, wi_creg, wi_buyr, wi_buyc, wi_ordi, wi_ordd, wi_admr, wi_admc];

fprintf(1, 'BROWSE\n');
prl(dat, i1);

fprintf(1, 'ORDER\n');
prl(dat, i2);

function [] = prl(dat, i)

for j=1:length(i)
  k = i(j);
  fprintf(1, '%20s %f\n', iname(k), dat(k));
end
