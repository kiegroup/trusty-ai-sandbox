//See https://github.com/typicode/json-server/issues/541#issuecomment-307300368
module.exports = (req, res, next) => {
    const _send = res.send
    res.send = function (body) {
      if (require('url').parse(req.originalUrl, true).path.endsWith('/model')) {
        try {
          const json = JSON.parse(body)
          if (Array.isArray(json)) {
            if (json.length === 1) {
              return _send.call(this, JSON.stringify(json[0]))
            } else if (json.length === 0) {
              return _send.call(this, '{}', 404)
            }
          }
        } catch (e) {}
      }
      return _send.call(this, body)
    }
    next()
  }