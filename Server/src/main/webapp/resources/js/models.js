function createFeed(url) {
    return {
        "url": url,
        "readItems": []
    }
}

function createGroup(name) {
    return {
        "name": name,
        "feeds": []
    }
}
