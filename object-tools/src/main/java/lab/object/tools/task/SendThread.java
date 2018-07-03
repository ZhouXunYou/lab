package lab.object.tools.task;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

import javassist.CannotCompileException;
import lab.object.tools.define.ClassDefine;
import lab.object.tools.define.Creator;
import lab.object.tools.define.FieldDefin;
import lab.utils.ObjectUtils;

public class SendThread implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(SendThread.class);
	private Creator creator;
	private int runCount = 0;
	private KafkaTemplate<String, String> kafkaTemplate;
	private ClassDefine classDefine;
	public SendThread(ClassDefine classDefine,KafkaTemplate<String, String> kafkaTemplate) throws CannotCompileException {
		this.creator = new Creator(classDefine.getClassName());
		for(FieldDefin fieldDefin:classDefine.getFields()) {
			creator.addField(fieldDefin.getFieldName(), fieldDefin.getType(), fieldDefin.getGenerateRule());
		}
		this.kafkaTemplate = kafkaTemplate;
		this.classDefine = classDefine;
		if(this.classDefine.getCount()<0) {
			this.classDefine.setCount(Integer.MAX_VALUE);
		}
	}
	@Override
	public void run() {
		LOG.info("Thread {},{} mission start",Thread.currentThread().getName(),Thread.currentThread().getId());
		
		while(runCount<this.classDefine.getCount()) {
			Object object = creator.createObject();
			String message = ObjectUtils.printObject(object);
			ListenableFuture<SendResult<String, String>> result = kafkaTemplate.send(new ProducerRecord<String, String>(this.classDefine.getTopic(), message));
			result.addCallback(new SuccessCallback<SendResult<String,String>>() {
				@Override
				public void onSuccess(SendResult<String,String> result) {
//					ProducerRecord<String,String> producerRecord = result.getProducerRecord();
					System.out.println(result.getRecordMetadata().timestamp());
					System.out.println(result.getRecordMetadata().offset());
					
				}
			}, new FailureCallback() {
				@Override
				public void onFailure(Throwable ex) {
					
				}
			});
			try {
				Thread.sleep(this.classDefine.getCycle());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runCount++;
		}
		LOG.info("Thread {},{} mission complete",Thread.currentThread().getName(),Thread.currentThread().getId());
	}

}
