import React from "react";
import { FeedbackData } from "@/schemas/message";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
} from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Badge } from "@/components/ui/badge";

export const FeedbackCard: React.FC<FeedbackData> = ({
  surveyName,
  isApproved,
}) => {
  return (
    <Card
      className={`bg-white p-4 shadow-lg rounded-md border border-gray-200 flex items-start space-x-4 ${
        isApproved === 0
          ? "border-gray-500"
          : isApproved === 1
          ? "border-green-500"
          : "border-red-500"
      }`}
    >
      <CardHeader>
        <div className="flex-1">
          <Label
            className={`mt-2 ${
              isApproved === 0
                ? "border-gray-500"
                : isApproved === 1
                ? "border-green-500"
                : "border-red-500"
            }`}
          >
            Feedback of your request from survey {surveyName}
          </Label>
        </div>
      </CardHeader>
      <CardContent>
        <div className="flex-1">
          <p className={`mt-2 text-gray-800`}>
            {isApproved === 0
              ? `Your request from survey ${surveyName} is yet to be approved`
              : isApproved === 1
              ? `Your request from survey ${surveyName} has been approved`
              : `Your request from survey ${surveyName} has been rejected`}
          </p>
        </div>
      </CardContent>
      <CardFooter>
        <Badge key="feedback" variant="secondary">
          Feedback
        </Badge>
      </CardFooter>
    </Card>
  );
};
