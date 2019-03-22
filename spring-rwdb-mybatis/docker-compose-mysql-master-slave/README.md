## FAQ

1. 在win10 中配置mysql主从的过程中遇到了 mysql master ：

    [ERROR] InnoDB: Write to file ./ibdata1failed at offset 0, 1048576 bytes should have been written, only 0 were written. Operating system error number 22. Check that your OS and file
    system support files of this size. Check also that the disk is not full or a disk quota exceeded.
    
    解决办法：
    
    在 master 的 my.cnf 中追加 `innodb_flush_method=O_DIRECT`