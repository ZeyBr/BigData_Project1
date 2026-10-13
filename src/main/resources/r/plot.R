local({
  library(lattice)
  values <- rep(NA_real_, 100L)
  function(index, value, output) {
    values[as.integer(index) + 1L] <<- as.numeric(value)
    points <- data.frame(x = 0:99, y = values)
    png(filename = output, width = 1000, height = 550)
    on.exit(dev.off())
    print(xyplot(
        y ~ x,
        data = points,
        type = "l",
        col = "#654321",
        lwd = 2,
        xlim = c(0, 99),
        xlab = "Index (0-99)",
        ylab = "Col-2",
        main = "SWE307 - Group 2",
        panel = function(x, y, ...) {
            panel.grid(h = -1, v = -1)
            panel.xyplot(x, y, ...)
      }))
    invisible(NULL)
  }
})
