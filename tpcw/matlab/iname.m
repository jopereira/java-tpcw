function [n] = iname(i)
%-------------------------------------------------------------------------
% function [n] = iname(i)
%
% Returns the string name of an interaction.
%-------------------------------------------------------------------------

n = {};

n = {n{:}, 'Initial Home'};
n = {n{:}, 'Admin Confirm'};
n = {n{:}, 'Admin Request'};
n = {n{:}, 'Best Sellers'};
n = {n{:}, 'Buy Confirm'};
n = {n{:}, 'Buy Request'};
n = {n{:}, 'Customer Regist.'};
n = {n{:}, 'Home'};
n = {n{:}, 'New Products'};
n = {n{:}, 'Order Display'};
n = {n{:}, 'Order Inquiry'};
n = {n{:}, 'Product Detail'};
n = {n{:}, 'Search Request'};
n = {n{:}, 'Search Results'};
n = {n{:}, 'Shopping Cart'};

n = n{i};

