
Simplified version of Java TPC-W implementation used at U. Minho. This version
was put together in http://gorda.di.uminho.pt and http://red.lsd.di.uminho.pt
projects by J. Pereira, R. Vilaça, M. Araujo, and others. It was based on the
installer at http://tpcw.deadpixel.de/ for PHARM TPC-W implementation. For
credits and redistribution license see the following included files:

* `legacy/docs/cred.html`
* `legacy/tpcw/COPYRIGHT`

The major goal of the changes in this version is to make it easier and less error prone
to setup, run, and modify by making sure that (i) each configuration parameter
is specified and stored only once; and (ii) source code can be edited with any
Java IDE (i.e. no `@filter@` substitution with ant).

To setup and install:

* edit `main.properties` and `tpcw.properties` (see included comments)
* run `ant inst`; `ant genimg`; and then `ant gendb`
* run `./rbe.sh`

Results analysis can be done using Python script at: https://gist.github.com/4086237

