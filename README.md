# magento-gatling

Gatling stress tests for Magento 1.

Inspiration and ideas by [creatuity/gatling_magento2](https://github.com/creatuity/gatling_magento2/).

## IMPORTANT

- The current version is _somehow_ developed for a project I am working on
- There might be small and huge bugs, there might be totally wrong implementations
- Ideas, improvements and pull requests welcome
- I'm a Gatling & Scala newbie

### Requirements

- Java JDK7
- [Gatling](http://gatling.io/) (developed under version `2.2.2`)

### Installation

[Download](http://gatling.io/docs/2.2.2/quickstart.html#getting-the-bundle) and [install](http://gatling.io/docs/2.2.2/quickstart.html#installing) Gatling.

Download/clone repository under your Gatling user simulations folder:

```
cd /path/to/gatling
cd user-files/simulations

git clone git@github.com:frozenminds/magento-gatling.git
```
[Generate](http://docs.magento.com/m1/ce/user_guide/marketing/google-sitemap.html) a Google `sitemap.xml` file from your Magento administration panel and download it to `/path/to/gatling/user-files/data/sitemap.xml`.

### Scenario

The current scenario simulates users browsing the site. Part of them will add products to cart and part of them will continue through the checkout process. The rest will abandon. There are also several pauses simulating the time to scan the current page.

### Usage

```
JAVA_OPTS="-Dbaseurl=https://www.domain.com" /path/to/gatling/bin/gatling.sh -m -sf /path/to/gatling/user-files/simulations/magento-gatling
```

Where options for Gatling are:

`-m` : Mute, doesn't interactively ask for anything

`-sf` : Simulation Folder, specifies where the simulation Scala files are

`-s` : Simulation to run (Scala class name for the simulation, in `namespace.class` format)

Additionally, `JAVA_OPTS` passes settings to the simulation itself, using `-D[option name]=[option value]`:

`baseurl` : Base URL of website, i.e. http://www.example.com. Default is http://localhost.

`realtimeratio` : Realtime ratio for simulated user pauses between actions. 1 is for normal delays, 0 is no delays - this is used as a multiplier for pauses so you can actually use values such as 0.5 or 2, etc. Default is 1.

`atonceusers` : The number of simulated users that spawn immediately (at once). Default is 1.

`rampusers` : The number of simulated users that spawn over the ramp time. Default is 10 users.

`rampseconds` : The duration of the ramp time in seconds. Default is 30 seconds.

`percentaddtocart` : Percent of users adding a product to cart. Happens when they visit a product or category page which has an add to cart form. Default is `30%`, configured as `30d`.

`percentaddtocartskip` : Percent of users who will skip adding a product to cart, they will just continue browsing the site. Default is `70%`, configured as `70d`.

`percentcheckoutguest` : Percent of users going through _Guest Checkout_. This percentage is from the total of users who added products to cart. **(Will be implemented soon)**

### ToDo

- Refactoring to move each step (browse, add to cart, checkout, etc.) into own `Object`
- Implement Guest Checkout
- Add more configuration options
- Fetch static resources, just like any browser would
- Implement user registration, login, logout
- Have an idea? [Post it](https://github.com/frozenminds/magento-gatling/issues)! :)

### Special thanks

- [gatling](http://gatling.io) developers
- [creatuity/gatling_magento2](https://github.com/creatuity/gatling_magento2/) for inspiring me
