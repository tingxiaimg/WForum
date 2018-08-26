const simple = (content, type) => {
    if (type === 'html' || type === 'HTML') return fromHtml(content)
    else if (type === 'markDown') return fromMarkDown(content)
    else throw new Error('文档类型不明！')
}
function fromHtml (content) {
    let reg = /<img.*?\/>/ig
    let url = []
    while (true) {
        let result = reg.exec(content)
        if (result === null) break
        let srcl = result[0].match(/src="(.*?)"/) || []
        if (srcl !== null) url.unshift(srcl)
    }
    // console.log(url)
    return {
        'content': content,
        'imgCount': url.length,
        'src': url,
        toString () {
            return this.content
        }
    }
}
function fromMarkDown (content) {
    // ![](/files/read/4)
    let reg = /!\[\]\((.*?)\)/ig
    let url = []
    while (true) {
        let result = reg.exec(content)
        // console.log(result)
        if (result === null) break
        let srcl = result[1] || ''
        if (srcl !== '') url.unshift(srcl)
    }
    // console.log(url)
    return {
        'content': content,
        'imgCount': url.length,
        'src': url,
        toString () {
            return this.content
        }
    }
}
export default simple
