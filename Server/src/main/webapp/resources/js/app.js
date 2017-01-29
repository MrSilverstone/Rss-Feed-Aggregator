$(document).ready( _ => {

    var appData = new Vue({
        el: "#app",

        data: {
            groups: [],
            currentFeeds: [],
            currentGroup: "",
            messages: {} // dic : key -> url, value -> object {title, description, link, author, guid}
        },

        methods: {
            setCurrentFeeds: function(groupName) {
                const group = this.groups.filter(e => {
                    return e.name == groupName
                })

                if (group != null && group.length > 0) {
                    this.currentGroup = groupName
                    this.currentFeeds = group[0].feeds
                }
            },

            displayFeedMessages: function(url) {
                return this.messages[url] != null
            },

            getFeedMessagesForUrl: function(url) {
                return this.messages[url]
            },

            getMessages: function(url) {
                if (this.messages[url] != null) {
                    this.messages[url] = null
                    this.$forceUpdate()

                } else {
                    requests.feeds.messages(url, messages => {
                        this.messages[url] = messages
                        this.$forceUpdate()
                    })
                }
            },

            add: function(groupName) {
                requests.groups.add(groupName, _ => {
                    const newGroup = createGroup(groupName)
                    this.groups.push(newGroup)
                })
            },

            deleteGroup: function(name) {
                requests.groups.delete(name, _ => {
                    this.groups = this.groups.filter( elem => {
                        return elem.name != name
                    })

                    if (this.currentGroup == name) {
                        this.currentGroup = ""
                        this.currentFeeds = []
                    }
                })
            },

            addFeed: function() {
                const url = $("#addFeedTF").val()
                const groupName = this.currentGroup

                if (url != "") {
                    requests.feeds.add(url, groupName, _ => {
                        this.groups.map( elem => {
                            if (elem.name == groupName) {
                                const newFeed = createFeed(url)
                                elem.feeds.push(newFeed)
                            }
                        })
                    })
                }
            },

            deleteFeed: function(url) {
                const groupName = this.currentGroup

                requests.feeds.delete(url, groupName, _ => {
                    var newFeeds = this.currentFeeds.filter(feed => {
                        return feed.url != url
                    })

                    this.currentFeeds = newFeeds

                    this.groups.map(group => {
                        if (group.name == groupName) {
                            group.feeds = newFeeds
                        }

                        return group
                    })

                    this.messages[url] = null
                })
            },

            logout: function() {
                $.removeCookie('token')
                $( location ).attr("href", "http://localhost:8080/");
            }
        }
    })

    const newGroupInput = $("#addGroupInput")

    // detect enter key on input to add new group
    newGroupInput.keydown(e => {
        if (e.keyCode == 13) {
            const groupName = newGroupInput.val()

            requests.groups.add(groupName, _ => {
                appData.groups.push(createGroup(groupName))
                newGroupInput.val("")
            })
        }
    })

    // init groups in menu
    requests.groups.get( groups => {
        appData.groups = groups
    })

    $(".groupLink").click( e => {
        console.log(e)
    })

})





   