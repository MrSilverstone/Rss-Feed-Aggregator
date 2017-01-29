$(document).ready( _ => {

    var appData = new Vue({
        el: "#app",

        data: {
            groups: [],
            currentFeeds: [],
            currentGroup: "",
            messages: [] // dic : key -> url, value -> object {title, description, link, author, guid}
        },

        methods: {
            setCurrentFeeds: function(groupName) {
                const group = this.groups.filter(e => {
                    return e.name == groupName
                })

                if (group.length > 0) {
                    this.currentGroup = groupName
                    this.currentFeeds = group[0].feeds
                }
            },

            getMessages: function(url) {
                requests.feeds.messages(url, messages => {
                    this.messages = messages
                })
            },

            add: function(groupName) {
                requests.groups.add(groupName, _ => {
                    const newGroup = createGroup(groupName)
                    this.groups.push(newGroup)
                })
            },

            deleteGroup: function() {
                const name = "CECI EST UN TEST"

                requests.groups.delete(name, _ => {
                    this.groups = this.groups.filter( elem => {
                        return elem.name != name
                    })
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





   