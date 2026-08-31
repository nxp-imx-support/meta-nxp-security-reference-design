Security Reference Design Meta Layer
======================

[![Documentation](https://img.shields.io/badge/docs-online-blue)](https://nxp-imx-support.github.io/meta-nxp-security-reference-design/)

This repository is a consolidation of various Security Reference Design projects delivered by NXP, to showcase the security capabilities of the NXP MPU/MCU devices.

**Full documentation is available online:** https://nxp-imx-support.github.io/meta-nxp-security-reference-design/

Dependencies
------------

Documentation
-------------

This repository includes comprehensive documentation for all Security Reference Design projects.

The documentation is published online and automatically updated on every push to the active branch:

    https://nxp-imx-support.github.io/meta-nxp-security-reference-design/

To build the documentation locally, install Sphinx and its dependencies:

    pip install sphinx sphinx-rtd-theme docxbuilder

To build HTML documentation:

    cd Documentation
    make html

The generated HTML documentation will be available at:

    Documentation/_build/html/index.html

To build PDF documentation:

    cd Documentation
    make latexpdf

The generated PDF documentation will be available in:

    Documentation/_build/latex/

Supported boards
----------------

Please see individual project's README for supported boards.
  
Releases
--------

Please see individual project's README for supported releases.

Usage
-------

Please see individual project's README for usage instructions
