This is the source for the GROOVE website, hosted at https://groove.cs.utwente.nl/

To host locally on a Windows machine
--------------

- It's all done in WSL

- The essential information can be found on https://docs.github.com/en/pages/setting-up-a-github-pages-site-with-jekyll/testing-your-github-pages-site-locally-with-jekyll

- To be sure you have all required development tools, you might need to run

  ```
  sudo apt-get install -y build-essential
  ```

  (see https://stackoverflow.com/questions/62965073/gemextbuilderror-error-failed-to-build-gem-native-extension-keep-getting)

- Invoke bundler with

  ```
  bundle install --path ~/.gem
  ```

  to avoid gems being installed in the possibly inaccessible `/etc/var/gem` directory (see https://stackoverflow.com/questions/16031061/force-bundler-to-install-gems-in-user-s-home-directory)

- You need a Gemfile, for which the following will do (see https://michaelriedl.com/2021/06/11/testing-jekyll.html):

  ```
  source "https://rubygems.org"
  gem "github-pages"
  gem "webrick"
  ```

- To get a webserver with the page up and running, invoke (see https://michaelriedl.com/2021/06/11/testing-jekyll.html)

  ```
  bundle exec jekyll serve
  ```

