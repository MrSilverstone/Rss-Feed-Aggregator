<!DOCTYPE html>
<html lang="en-US">
<head>
    <meta charset="utf-8">
    <title>RSS Feed Aggregator</title>

    <script src="https://unpkg.com/vue/dist/vue.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src=" https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
    <script src="${pageContext.request.contextPath}/app-resources/js/models.js"></script>
    <script src="${pageContext.request.contextPath}/app-resources/js/requests.js"></script>
    <script src="${pageContext.request.contextPath}/app-resources/js/app.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app-resources/css/style.css"/>
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:300,400,500,700" type="text/css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
</head>
<body>

<!-- Uses a header that contracts as the page scrolls down. -->
<style>
    .demo-layout-waterfall .mdl-layout__header-row .mdl-navigation__link:last-of-type  {
        padding-right: 0;
    }
</style>

<div class="demo-layout-waterfall mdl-layout mdl-js-layout" id="app">
    <header class="mdl-layout__header mdl-layout__header--waterfall">
        <!-- Top row, always visible -->
        <div class="mdl-layout__header-row">
            <!-- Title -->
            <span class="mdl-layout-title">RSS Feed Aggregator</span>
            <div class="mdl-layout-spacer"></div>
            <nav class="mdl-navigation mdl-layout--large-screen-only">
                <a id="logoutBtn" class="mdl-navigation__link" href="" v-on:click="logout">Log out</a>
            </nav>
        </div>
    </header>
    <div class="mdl-layout__drawer">
        <span class="mdl-layout-title">My groups</span>
        <nav class="mdl-navigation">
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--expandable
                  mdl-textfield--floating-label mdl-textfield--align-right">
                <label class="mdl-button mdl-js-button mdl-button--icon"
                       for="addGroupInput" id="addGroupButton">
                    <i class="material-icons">add</i>
                </label>
                <div class="mdl-textfield__expandable-holder">
                    <input class="mdl-textfield__input" type="text" name="sample"
                           id="addGroupInput">
                </div>
            </div>

            <div v-for="group in groups" v-on:click="setCurrentFeeds(group.name)">
                <span class="mdl-navigation__link">{{ group.name }}
                    <i class="material-icons removeGroup" v-on:click="deleteGroup(group.name)">clear</i>
                </span>
            </div>

        </nav>
    </div>
    <main class="mdl-layout__content">
        <div class="mainContent" v-if="currentGroup != ''">
            <h4 style="margin-left: 40px;">RSS Feeds for group {{currentGroup}}</h4>
            <div style="margin-left: 40px;">
                <form action="#">
                    <div class="mdl-textfield mdl-js-textfield">
                        <input class="mdl-textfield__input" type="text" id="addFeedTF">
                        <label class="mdl-textfield__label" for="addFeedTF">Feed url</label>
                    </div>
                </form>
                <button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" v-on:click="addFeed">
                    Add feed
                </button>
            </div>
            <!-- Colored raised button -->
            <ul class="feed-list mdl-list">
                <li class="mdl-list__item" v-for="feed in currentFeeds">
                <span class="mdl-list__item-primary-content">
                    <div class="feedTitle">
                        <div class="inlineBlock rssIcon"><i class="material-icons mdl-list__item-icon">rss_feed</i></div>

                        <div class="rss-link inlineBlock" v-on:click="getMessages(feed.url)">
                                {{feed.url}}
                        </div>
                        <div class="inlineBlock clearFeedIcon" v-on:click="deleteFeed(feed.url)"><i class="material-icons">clear</i></div>

                        <div class="feed-message" v-if="displayFeedMessages(feed.url)">
                            <div v-for="message in getFeedMessagesForUrl(feed.url)">
                                <p><a v-bind:href="message.link">{{message.title}}</a></p>
                            </div>
                        </div>
                        <div v-else>
                        </div>
                    </div>
                </span>
                </li>
            </ul>
        </div>
        <div v-else>
            <h4 style="margin-left: 40px;">Choose a group to display feeds</h4>
        </div>

    </main>
</div>

</body>

</html>
  