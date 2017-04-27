sudo apt-get update
sudo apt-get install default-jre

#setting JAVA_HOME
if [ ! -f /etc/profile.d/java.sh ]; then
	echo -n "JAVA_HOME=" > /etc/profile.d/java.sh
	sudo update-alternatives --list java | grep default-jre | awk '{
		sub("/jre/.*", "");
		print $0;
	}' >> /etc/profile.d/java.sh
fi

source /etc/profile.d/java.sh
echo end setting $JAVA_HOME

