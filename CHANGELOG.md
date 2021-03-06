# dev
  - added initial (liberal) support for ARIA in SVG
  - dropped the error for meta[http-equiv=X-UA-Compatible]
  - added version info to jar manifest file
  - make nu.validator.client.TestRunner exit non-zero for test failures

# 06 October 2014
  - brought reporting of bad IDs in `form` attr into compliance with spec
    (see https://github.com/validator/validator.github.io/issues/8
    and thanks again to https://github.com/cavweb20)

# 01 September 2014
  - fixed bug that broke json & xml message output
    (see https://github.com/validator/validator.github.io/issues/5
    and thanks to https://github.com/cavweb20)

# 25 August 2014
  - added support for the `<picture>` element
  - improved ARIA support for various table elements
  - made refinements to outline handling
  - added experimental warnings for some heading/outline issues
  - improved checking for `meta@name` and `link@rel` values
  - CLI now exits with `1` if any errors are found
  - CLI no longer says `XHTML element` in error messages
  - switched to galimatias for URL checking
  - updated to latest ICU4J
  - release now includes WAR file

# 02 February 2014
  - initial `vnu.jar` release
