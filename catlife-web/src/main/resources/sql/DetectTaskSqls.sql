-- 查找识别任务的结果（猫）
select cat.catclass as catclass, introduction, headphoto
from detectcattask join cat on detectcattask.resultclass = cat.catclass
where detectcattask.taskid = #{taskId};